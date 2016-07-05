import pagerank
from copy import deepcopy


def query(dictionary, text, pagerank, terms, indices):
    incidence = []
    for i in range(len(terms)):
        term = terms[i].lower()
        incidence.append([])
        # Loop over the anchors
        for anchor in dictionary:
            if term in anchor.lower().split(" "):
                for document in dictionary[anchor]:
                    if document not in incidence[i]:
                        incidence[i].append(document)
        # Loop over the plain text
        for data in text:
            if term in data.lower().split(" "):
                for document in text[data]:
                    if document not in incidence[i]:
                        incidence[i].append(document)
    # Find the smallest list
    minimum = 999999999999999999
    index = 0
    for i in range(len(incidence)):
        if len(incidence[i]) < minimum:
            minimum = len(incidence[i])
            index = i

    result = deepcopy(incidence[index])
    # Find all the matching documents
    for term in incidence[index]:
        for j in range(len(incidence)):
            # Remove the term since it's not in all documents
            if term not in incidence[j]:
                result.remove(term)
                break
    # Sort based on pagerank
    result.sort(key=lambda x: pagerank[indices[x]], reverse=True)

    # Return the top 10
    return result[0:10]


def perform_query(dictionary, text, pr, terms, indices):
    result = query(dictionary, text, pr, terms, indices)
    return result
