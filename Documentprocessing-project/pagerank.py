import webcrawler

ALPHA = 0.1
DIFFERENCE = 0.001


def compute_probability_matrix(web_graph):
    # Index the links
    keys = list(web_graph.keys())
    indices = {keys[i]: i for i in range(len(keys))}
    # Calculate probability matrix for all the non-zero values
    probabilities = []
    for key in keys:
        pages = web_graph[key]
        probabilities.append([(indices[p],
                              (1 / len(pages)) - (ALPHA / len(pages)))
                              for p in pages])
    return (indices, probabilities)


def compute_pagerank(indices, probabilities):
    # Initialize vector_pi
    vec_pi = [0] * len(probabilities)
    vec_pi[0] = 1

    result = [0] * len(vec_pi)
    diff = 9001
    # Calculate vec_pi until we reach a steady state
    while diff > (DIFFERENCE * len(vec_pi) / 2):
        for i in range(len(vec_pi)):
            pos = 0
            for j in range(len(probabilities)):
                if (pos <= len(probabilities[i]) - 1 and
                   probabilities[i][pos][0] == i):
                    value = probabilities[j][pos][1]
                    pos += 1
                else:
                    value = ALPHA / (len(vec_pi) - len(probabilities[i]))
                result[i] += vec_pi[i] * value

        diff = 0
        for i in range(len(vec_pi)):
            diff += abs(vec_pi[i] - result[i])
        vec_pi = result

    return vec_pi


def print_stats(dictionary, web_graph):
    print('Number of documents crawled: {}'.format(len(web_graph)))
    print('Number of distinct terms: {}'.format(len(dictionary)))
    # As this is a closed network the mean number of incoming links
    # and links per page is the same
    links_pp = 0
    for key in web_graph:
        links_pp += len(web_graph[key])
    links_pp /= len(web_graph)
    print('Mean number of incoming links: {}'.format(links_pp))
    print('Mean number of links per page: {}'.format(links_pp))


def perform_pagerank(url):
    dictionary, web_graph, text = webcrawler.crawl(url)
    indices, probabilities = compute_probability_matrix(web_graph)
    pagerank = compute_pagerank(indices, probabilities)
    print_stats(dictionary, web_graph)

    return (dictionary, text, pagerank, indices)
